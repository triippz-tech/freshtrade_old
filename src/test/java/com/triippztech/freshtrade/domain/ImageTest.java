package com.triippztech.freshtrade.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.triippztech.freshtrade.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ImageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Image.class);
        Image image1 = new Image();
        image1.setId(UUID.randomUUID());
        Image image2 = new Image();
        image2.setId(image1.getId());
        assertThat(image1).isEqualTo(image2);
        image2.setId(UUID.randomUUID());
        assertThat(image1).isNotEqualTo(image2);
        image1.setId(null);
        assertThat(image1).isNotEqualTo(image2);
    }
}
