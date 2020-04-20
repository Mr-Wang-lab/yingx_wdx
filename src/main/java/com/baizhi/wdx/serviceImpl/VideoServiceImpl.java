package com.baizhi.wdx.serviceImpl;

import com.baizhi.wdx.annotation.AddCache;
import com.baizhi.wdx.annotation.AddLog;
import com.baizhi.wdx.annotation.DelCache;
import com.baizhi.wdx.dao.VideoMapper;
import com.baizhi.wdx.entity.Video;
import com.baizhi.wdx.entity.VideoExample;
import com.baizhi.wdx.po.VideoPo;
import com.baizhi.wdx.repository.VideoRepository;
import com.baizhi.wdx.service.VideoService;
import com.baizhi.wdx.util.AliyunOssUtil;
import com.baizhi.wdx.util.InterceptVideoPhotoUtil;
import com.baizhi.wdx.vo.VideoVo;
import com.sun.javafx.collections.MappingChange;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.ibatis.session.RowBounds;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.*;

@Service
@Transactional
public class VideoServiceImpl implements VideoService {
    @Resource
    ElasticsearchTemplate elasticsearchTemplate;

    @Resource
    VideoRepository videoRepository;
    @Resource
    VideoMapper videoMapper;

    @Resource
    HttpSession session;

    @Override
    public List<Video> querySearch(String content) {

        //查询的条件
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withIndices("yingxv")  //指定索引名
                .withTypes("video")   //指定索引类型
                .withQuery(QueryBuilders.queryStringQuery(content).field("title").field("brief"))  //搜索的条件
                .build();

        //查询
        List<Video> videos = elasticsearchTemplate.queryForList(nativeSearchQuery, Video.class);

        return videos;
    }
    @Override
    public List<Video> querySearchs(String content) {

        //处理高亮的操作
        HighlightBuilder.Field field = new HighlightBuilder.Field("*");
        field.preTags("<span style='color:red'>"); //前缀
        field.postTags("</span>"); //后缀

        //查询的条件
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withIndices("yingxv")  //指定索引名
                .withTypes("video")   //指定索引类型
                .withQuery(QueryBuilders.queryStringQuery(content).field("title").field("brief"))  //搜索的条件
                .withHighlightFields(field)  //处理高亮
                //.withFields("title","brief","cover")  //查询返回指定字段
                .build();

        //高亮查询
        AggregatedPage<Video> videoList = elasticsearchTemplate.queryForPage(nativeSearchQuery, Video.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {

                ArrayList<Video> videos = new ArrayList<>();


                //获取查询结果
                SearchHit[] hits = searchResponse.getHits().getHits();

                for (SearchHit hit : hits) {

                    //System.out.println("====="+hit.getSourceAsMap()); //原始数据
                    //System.out.println("-------"+hit.getHighlightFields()); //高亮数据

                    //原始数据
                    Map<String, Object> map = hit.getSourceAsMap();

                    //处理普通数据

                    String id = map.get("id")!=null?map.containsKey("id") ? map.get("id").toString() : null:null;
                    String title = map.get("title")!=null?map.containsKey("title") ? map.get("title").toString() : null:null;
                    String brief = map.get("brief")!=null?map.containsKey("brief") ? map.get("brief").toString() : null:null;
                    String path = map.get("path")!=null?map.containsKey("path") ? map.get("path").toString() : null:null;
                    String cover = map.get("cover")!=null?map.containsKey("cover") ? map.get("cover").toString() : null:null;
                    String categoryId = map.get("categoryId")!=null?map.containsKey("categoryId") ? map.get("categoryId").toString() : null:null;
                    String groupId = map.get("groupId")!=null?map.containsKey("groupId") ? map.get("groupId").toString() : null:null;
                    String userId = map.get("userId")!=null?map.containsKey("userId") ? map.get("userId").toString() : null:null;

                    //处理日期操作
                    Date date = null;

                    if(map.get("publishDate")!=null){
                        if (map.containsKey("publishDate")) {
                            String publishDateStr = map.get("publishDate").toString();

                            //处理日期转换
                            Long aLong = Long.valueOf(publishDateStr);
                            date = new Date(aLong);
                        }
                    }

                    //封装video对象
                    Video video = new Video(id, title, brief, path, cover, date, categoryId, groupId, userId);

                    //处理高亮数据
                    Map<String, HighlightField> highlightFields = hit.getHighlightFields();

                    if(title!=null){
                        if (highlightFields.get("title") != null) {
                            String titles = highlightFields.get("title").fragments()[0].toString();
                            video.setTitle(titles);
                        }
                    }

                    if(brief!=null){
                        if (highlightFields.get("brief") != null) {
                            String briefs = highlightFields.get("brief").fragments()[0].toString();
                            video.setBrief(briefs);
                        }

                    }

                    //将对象放入集合
                    videos.add(video);

                }
                //强转 返回
                return new AggregatedPageImpl<T>((List<T>) videos);
            }
        });

        return videoList.getContent();
    }
    //分页查询
    @AddCache
    @Override
    public HashMap<String, Object> queryByPage(Integer page, Integer rows) {

        System.out.println("video page");
        HashMap<String, Object> map = new HashMap<>();
        VideoExample example = new VideoExample();
        //总条数 records
        Integer records = videoMapper.selectCountByExample(example);
        map.put("records",records);


        //总页数totals
        Integer totals = records % rows == 0 ? records / rows : records / rows + 1;

        map.put("totals",totals);
        //当前页
        map .put("page",page);
        //数据 rows 分页
        RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
        List<Video> vid = videoMapper.selectByRowBounds(new Video(), rowBounds);
        map.put("rows",vid);

        return map;
    }
    @AddLog("添加视频")
    @DelCache
    @Override
    public String add(Video video) {

        String uid = UUID.randomUUID().toString();
        video.setId(uid);
        video.setBrief(video.getBrief());
        video.setGroupId("1");
        video.setPublishDate(new Date());
        video.setCategoryId("1");
        video.setUserId("1");
        videoMapper.insert(video);
        //向es中构建索引
        videoRepository.save(video);
        return uid;
    }
    @AddLog("修改视频")
    @DelCache
    @Override
    public void update(Video video) {

        System.out.println(video+"videoController");
        videoMapper.updateByPrimaryKeySelective(video);
    }

    @Override
    public void uploadVdieo(MultipartFile path, String id, HttpServletRequest request) {

        //上传到阿里云

        //获取文件名
        String filename = path.getOriginalFilename();
        String newName=new Date().getTime()+"-"+filename;

        /*1.视频上传至阿里云
         *上传字节数组
         * 参数：
         *   bucket:存储空间名
         *   headImg: 指定MultipartFile类型的文件
         *   fileName:  指定上传文件名  可以指定上传目录：  目录名/文件名
         * */
        AliyunOssUtil.uploadFileBytes("yingx-186wdx",path,"video/"+newName);


        //频接视频完整路径
        String netFilePath="https://yingx-186wdx.oss-cn-beijing.aliyuncs.com/video/"+newName;



        /*2.截取视频第一帧做封面
         * 获取指定视频的帧并保存为图片至指定目录
         * @param videofile 源视频文件路径
         * @param framefile 截取帧的图片存放路径
         * */
        String realPath = session.getServletContext().getRealPath("/upload/cover");

        File file = new File(realPath);
        if(!file.exists()){
            file.mkdirs();
        }

        //频接本地存放路径    D:\动画.jpg
        // 1585661687777-好看.mp4
        String[] names = newName.split("\\.");
        String interceptName=names[0];
        String coverName=interceptName+".jpg";  //频接封面名字
        String coverPath= realPath+"\\"+coverName;  //频接视频封面的本地绝对路径


        //截取封面保存到本地
        try {
            InterceptVideoPhotoUtil.fetchFrame(netFilePath,coverPath);
        } catch (Exception e) {
            e.printStackTrace();
        }


        /*3.将封面上传至阿里云
         *上传本地文件
         * 参数：
         *   bucket:  存储空间名
         *   fileName:  指定上传文件名  可以指定上传目录：  目录名/文件名
         *   localFilePath: 指定本地文件路径
         * */
        AliyunOssUtil.uploadFile("yingx-186wdx","photo/"+coverName,coverPath);

        //4.删除本地文件
        File file1 = new File(coverPath);
        //判断是一个文件，并且文件存在
        if(file1.isFile()&&file1.exists()){
            //删除文件
            boolean isDel = file1.delete();
            System.out.println("删除："+isDel);
        }

        //5.修改视频信息
        //添加修改条件
        VideoExample example = new VideoExample();
        example.createCriteria().andIdEqualTo(id);

        //修改的结果
        Video video = new Video();

        video.setPath("https://yingx-186wdx.oss-cn-beijing.aliyuncs.com/video/"+newName);
        video.setCover("https://yingx-186wdx.oss-cn-beijing.aliyuncs.com/photo/"+coverName);

        //调用修改方法
        videoMapper.updateByExampleSelective(video,example);


    }

    @Override
    public void uploadVdieos(MultipartFile path, String id, HttpServletRequest request) {
        //上传到阿里云

        //获取文件名
        String filename = path.getOriginalFilename();
        String newName=new Date().getTime()+"-"+filename;

        /*1.视频上传至阿里云
         *上传字节数组
         * 参数：
         *   bucket:存储空间名
         *   headImg: 指定MultipartFile类型的文件
         *   fileName:  指定上传文件名  可以指定上传目录：  目录名/文件名
         * */
        AliyunOssUtil.uploadFileBytes("yingx-186wdx",path,"video/"+newName);


        //频接视频完整路径
        String netFilePath="https://yingx-186wdx.oss-cn-beijing.aliyuncs.com/video/"+newName;


        //频接本地存放路径    D:\动画.jpg
        // 1585661687777-好看.mp4
        String[] names = newName.split("\\.");
        String interceptName=names[0];
        String coverName=interceptName+".jpg";  //频接封面名字

        /*2.截取视频第一帧做封面
         * 视频截取  并上传至阿里云
         * 参数：
         *   bucker: 存储空间名
         *   fileName:远程文件文件名    目录名/文件名
         *   coverName：截取的封面名
         * */
        AliyunOssUtil.videoCoverIntercept("yingx-186wdx","video/"+newName,"photo/"+coverName);

        //5.修改视频信息
        //添加修改条件
        VideoExample example = new VideoExample();
        example.createCriteria().andIdEqualTo(id);

        //修改的结果
        Video video = new Video();

        video.setPath("https://yingx-186wdx.oss-cn-beijing.aliyuncs.com/video/"+newName);
        video.setCover("https://yingx-186wdx.oss-cn-beijing.aliyuncs.com/photo/"+coverName);

        //调用修改方法
        videoMapper.updateByExampleSelective(video,example);

        //设置id
        video.setId(id);
        Video videos = videoMapper.selectOne(video);

        //向es中构建索引
        videoRepository.save(videos);



    }
    @AddLog("删除视频")
    @DelCache
    @Override
    public HashMap<String, Object> delete(Video video) {


        System.out.println("sahnchu video --------------");
        HashMap<String, Object> map = new HashMap<>();
        try {

            //设置条件
            VideoExample example = new VideoExample();
            example.createCriteria().andIdEqualTo(video.getId());
            //根据id查询视频数据
            Video videos = videoMapper.selectOneByExample(example);

            //1.删除数据
            videoMapper.deleteByExample(example);


            //字符串拆分
            String[] pathSplit = videos.getPath().split("/");
            String[] coverSplit = videos.getCover().split("/");
            System.out.println(coverSplit+"------------------cover");
            /*String aaa="video/"+pathSplit[4];
            String aaas="photo/"+coverSplit[4];*/
            String videoName=pathSplit[pathSplit.length-2]+"/"+pathSplit[pathSplit.length-1];
            String coverName=coverSplit[coverSplit.length-2]+"/"+coverSplit[coverSplit.length-1];
//
//            System.out.println(videoName);
//            System.out.println(coverName);
//

            /*2.删除视频
             * 删除阿里云文件
             * 参数：
             *   bucker: 存储空间名
             *   fileName:文件名    目录名/文件名
             * */
            AliyunOssUtil.delete("yingx-186wdx",videoName);
            //删除索引
            videoRepository.delete(video);
            /*3.删除封面
             * 删除阿里云文件
             * 参数：
             *   bucker: 存储空间名
             *   fileName:文件名    目录名/文件名
             * */
            AliyunOssUtil.delete("yingx-186wdx",coverName);

            map.put("status", "200");
            map.put("message", "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", "400");
            map.put("message", "删除失败");
        }
        return map;
    }
    @AddCache
    @Override
    public List<VideoVo> queryByReleaseTime() {

        List<VideoPo> videoPos = videoMapper.queryByReleaseTime();

        ArrayList<VideoVo> videoVosList = new ArrayList<VideoVo>();

        //点赞数
        //遍历视频id
        for (VideoPo v : videoPos) {

            String id = v.getId();
            //根据视频id查询视频的点赞数
            Integer likeCount=18;

            //给vo赋值
            VideoVo videoVo = new VideoVo(v.getId(), v.getVTitle(), v.getVBrief(), v.getVPath(),
                    v.getVCover(), v.getVPublishDate(), likeCount, v.getCateName(), v.getHeadImg()
            );
            videoVosList.add(videoVo);
        }

        return videoVosList;
    }
}
