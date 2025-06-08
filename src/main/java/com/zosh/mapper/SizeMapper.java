package com.zosh.mapper;

import com.zosh.dto.SizeDTO;
import com.zosh.modal.Size;

public class SizeMapper {

    public static SizeDTO toDTO(Size size) {
        SizeDTO dto = new SizeDTO();
        dto.setId(size.getId());
        dto.setName(size.getName());
        dto.setQuantity(size.getQuantity());
        return dto;
    }

    public static Size toEntity(SizeDTO dto) {
        Size size = new Size();
        size.setId(dto.getId()); // Optional if you're doing an update
        size.setName(dto.getName());
        size.setQuantity(dto.getQuantity());
        return size;
    }
}
