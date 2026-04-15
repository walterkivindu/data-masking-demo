package com.walter.datamaskingdemo.api;

import com.walter.datamaskingdemo.annotation.masking.Mask;
import com.walter.datamaskingdemo.annotation.masking.Maskable;

@Maskable
public record Photo(String albumId, String id, String title, @Mask(prefix = 5, suffix = 4, maskChar = '*') String url,
                    String thumbnailUrl) {
}
