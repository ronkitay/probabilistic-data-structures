package org.ronkitay.lectures.probabilisticdatastructures;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.PrimitiveSink;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.ronkitay.lectures.probabilisticdatastructures.StringFunnel.STRING_FUNNEL;

/**
 * @author Ron Kitay
 * @since 10-Sep-2021.
 */
public class BloomFilterUsage {

    public static void main(String[] args) {
        BloomFilter<String> bloomFilter = BloomFilter.create(STRING_FUNNEL, 1_000_000, 0.01d);

        try (Stream<String> lines = Files.lines(Paths.get(BloomFilterUsage.class.getClassLoader().getResource("random-words.txt").getPath()), StandardCharsets.UTF_8)) {
            lines.forEachOrdered(bloomFilter::put);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (Stream<String> lines = Files.lines(Paths.get(BloomFilterUsage.class.getClassLoader().getResource("test-words.txt").getPath()), StandardCharsets.UTF_8)) {
            lines.forEachOrdered(line -> {
                if (bloomFilter.mightContain(line)) {
                    System.out.println("'" + line + "' might exist!");
                }
                else {
                    System.out.println("'" + line + "' does not exist!");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

enum StringFunnel implements Funnel<String>{
    STRING_FUNNEL;

    @Override
    public void funnel(String from, PrimitiveSink into) {
        into.putString(from, StandardCharsets.UTF_8);
    }
}
