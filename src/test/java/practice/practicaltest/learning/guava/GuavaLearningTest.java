package practice.practicaltest.learning.guava;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.Lists;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GuavaLearningTest {

    @Test
    @DisplayName("주어진 리스트를 원하는 크기로 나눌 수 있다.")
    void partition() throws Exception {
        // given
        List<Integer> givenIntegers = List.of(1, 2, 3, 4, 5, 6);

        // when
        List<List<Integer>> result = Lists.partition(givenIntegers, 3);

        // then
        assertThat(result).hasSize(2)
                          .isEqualTo(List.of(List.of(1, 2, 3), List.of(4, 5, 6)));
    }

    @Test
    @DisplayName("원하는 크기가 나누어 떨어지지 않는 경우, 나머지 크기만큼 나눈다.")
    void partition2() throws Exception {
        // given
        List<Integer> givenIntegers = List.of(1, 2, 3, 4, 5, 6);

        // when
        List<List<Integer>> result = Lists.partition(givenIntegers, 4);

        // then
        assertThat(result).hasSize(2)
                          .isEqualTo(List.of(List.of(1, 2, 3, 4), List.of(5, 6)));
    }

}
