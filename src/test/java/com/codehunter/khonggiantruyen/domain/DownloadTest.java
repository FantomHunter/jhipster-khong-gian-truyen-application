package com.codehunter.khonggiantruyen.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.codehunter.khonggiantruyen.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DownloadTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Download.class);
        Download download1 = new Download();
        download1.setId(1L);
        Download download2 = new Download();
        download2.setId(download1.getId());
        assertThat(download1).isEqualTo(download2);
        download2.setId(2L);
        assertThat(download1).isNotEqualTo(download2);
        download1.setId(null);
        assertThat(download1).isNotEqualTo(download2);
    }
}
