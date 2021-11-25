package com.codehunter.khonggiantruyen.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.codehunter.khonggiantruyen.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ResourceDownloadTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResourceDownload.class);
        ResourceDownload resourceDownload1 = new ResourceDownload();
        resourceDownload1.setId(1L);
        ResourceDownload resourceDownload2 = new ResourceDownload();
        resourceDownload2.setId(resourceDownload1.getId());
        assertThat(resourceDownload1).isEqualTo(resourceDownload2);
        resourceDownload2.setId(2L);
        assertThat(resourceDownload1).isNotEqualTo(resourceDownload2);
        resourceDownload1.setId(null);
        assertThat(resourceDownload1).isNotEqualTo(resourceDownload2);
    }
}
