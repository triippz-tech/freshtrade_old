package com.triippztech.freshtrade.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.triippztech.freshtrade.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Item.class);
        Item item1 = new Item();
        item1.setId(UUID.randomUUID());
        Item item2 = new Item();
        item2.setId(item1.getId());
        assertThat(item1).isEqualTo(item2);
        item2.setId(UUID.randomUUID());
        assertThat(item1).isNotEqualTo(item2);
        item1.setId(null);
        assertThat(item1).isNotEqualTo(item2);
    }
}
