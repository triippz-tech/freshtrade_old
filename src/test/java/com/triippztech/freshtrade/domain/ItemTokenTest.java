package com.triippztech.freshtrade.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.triippztech.freshtrade.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ItemTokenTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemToken.class);
        ItemToken itemToken1 = new ItemToken();
        itemToken1.setId(1L);
        ItemToken itemToken2 = new ItemToken();
        itemToken2.setId(itemToken1.getId());
        assertThat(itemToken1).isEqualTo(itemToken2);
        itemToken2.setId(2L);
        assertThat(itemToken1).isNotEqualTo(itemToken2);
        itemToken1.setId(null);
        assertThat(itemToken1).isNotEqualTo(itemToken2);
    }
}
