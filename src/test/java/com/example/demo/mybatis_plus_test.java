package com.example.demo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.bili.ItemDO;
import com.example.demo.mapper.bili.ItemMapper;
import com.example.demo.service.bili.MybatisPlusService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.example.demo.common.Utils.import_json_bvid;
import static com.example.demo.common.Utils.walk_dir;

@SpringBootTest
public class mybatis_plus_test {
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private MybatisPlusService mybatisPlusService;
    @Test
    public void testSelectPage() {
        QueryWrapper<ItemDO> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .like("part", "npm");
        Page<ItemDO> page = new Page(1,10);
        Page<ItemDO> userPage = itemMapper.selectPage(page, queryWrapper);
        //返回对象得到分页所有数据
        long pages = userPage.getPages(); //总页数
        long current = userPage.getCurrent(); //当前页
        List<ItemDO> records = userPage.getRecords(); //查询数据集合
        long total = userPage.getTotal(); //总记录数
        boolean hasNext = userPage.hasNext();  //下一页
        boolean hasPrevious = userPage.hasPrevious(); //上一页

        System.out.println(pages);
        System.out.println(current);
        System.out.println(records);
        System.out.println(total);
        System.out.println(hasNext);
        System.out.println(hasPrevious);
    }
    @Test
    public void test_query() {
        Page<ItemDO> query = mybatisPlusService.query("npm", 1, 10);
        System.out.println(query);
    }
    @Test
    public void list() {
        QueryWrapper<ItemDO> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .like("part", "npm");
        List<ItemDO> pageDOList = itemMapper.selectList(queryWrapper);
        System.out.println(pageDOList);
    }
    @Test
    public void testAdd() {
        ItemDO pageDO = new ItemDO();
        pageDO.setUrl("test");
        int insert = itemMapper.insert(pageDO);
        System.out.println(insert);
    }

    public void insertItem(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        List<ItemDO> items = import_json_bvid(path);
//        System.out.println(items);
        items.forEach(item -> {
            int insert = itemMapper.insert(item);
//            System.out.println(insert);
        });
    }
    @Test
    public void insertAll() throws IOException {
        String filePath = "D:\\T\\Documents\\VSCode\\js\\bili\\up\\37974444\\bvid\\BV1a34y167AZ.json";
//        new Path(filePath);
        insertItem(filePath);
    }
    @Test
    public void test_insert_json() throws IOException {
        List<String> ls = new ArrayList<>();
        ls = Arrays.asList("302417610", "37974444");
        String dir = "D:\\T\\Documents\\VSCode\\js\\bili\\up";

        ls.forEach(mid -> {
            Path path = Paths.get(dir, mid, "bvid");
            try {
                List<Path> paths = walk_dir(path);
                paths.forEach(p -> {
                    try {
                        List<ItemDO> items = import_json_bvid(p);
                        items.forEach(item -> {
                            int insert = itemMapper.insert(item);
                        });
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        System.out.println("done");
    }

    @Test
    public void testSelect1() {
        List<ItemDO> pages = itemMapper.selectBatchIds(Arrays.asList(1236960814, 1236960832));
        System.out.println(pages);
    }

    @Test
    public void testSelect2() {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("cid",1236960814);
//        columnMap.put("age",20);
        List<ItemDO> pages = itemMapper.selectByMap(columnMap);
        System.out.println(pages);
    }

    @Test
    public void testSelectMaps() {
        QueryWrapper<ItemDO> queryWrapper = new QueryWrapper<>();
//        queryWrapper
//                .select("url", "part")
//                .like("part", "npm");
        queryWrapper
                .like("part", "npm");
        List<Map<String, Object>> maps = itemMapper.selectMaps(queryWrapper);//返回值是Map列表
        maps.forEach(System.out::println);
    }
}
