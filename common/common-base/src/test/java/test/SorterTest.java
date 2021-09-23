package test;

import com.blue.base.common.base.ListSorter;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * @author 排序测试类
 */
@SuppressWarnings("ALL")
public class SorterTest {

    public static void main(String[] args) {

        List<String> list = Arrays.asList("今", "天", "呢", "什", "么", "吃", "晚", "上");

        System.err.println(ListSorter.sort(list, Locale.CANADA));

    }

}
