package io.github.PiotrGamorski.model.projection;

import io.github.PiotrGamorski.model.GroupOfTasks;
import io.github.PiotrGamorski.model.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class GroupOfTasksReadModelTest {
    @Test
    @DisplayName("should create null deadline for group when no tasks deadlines")
    void constructor_noDeadlines_createsNullDeadline(){
        // given
        var source = new GroupOfTasks();
        source.setDescription("foo");
        source.setTasks(Set.of(new Task("bar", null)));
        // when
        var result = new GroupOfTasksReadModel(source);
        // then
        assertThat(result).hasFieldOrPropertyWithValue("deadline", null);
    }
}