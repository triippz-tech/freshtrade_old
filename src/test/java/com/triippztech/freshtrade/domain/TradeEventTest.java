package com.triippztech.freshtrade.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.triippztech.freshtrade.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class TradeEventTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TradeEvent.class);
        TradeEvent tradeEvent1 = new TradeEvent();
        tradeEvent1.setId(UUID.randomUUID());
        TradeEvent tradeEvent2 = new TradeEvent();
        tradeEvent2.setId(tradeEvent1.getId());
        assertThat(tradeEvent1).isEqualTo(tradeEvent2);
        tradeEvent2.setId(UUID.randomUUID());
        assertThat(tradeEvent1).isNotEqualTo(tradeEvent2);
        tradeEvent1.setId(null);
        assertThat(tradeEvent1).isNotEqualTo(tradeEvent2);
    }
}
