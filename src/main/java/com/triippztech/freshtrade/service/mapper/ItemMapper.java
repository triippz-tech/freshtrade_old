package com.triippztech.freshtrade.service.mapper;

import com.triippztech.freshtrade.domain.Item;
import com.triippztech.freshtrade.service.dto.item.ItemDetailDTO;
import com.triippztech.freshtrade.service.dto.item.ListItemDTO;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ItemMapper {

    public List<ListItemDTO> itemsToItemDTOs(List<Item> items) {
        return items.stream().filter(Objects::nonNull).map(this::itemToListItemDTO).collect(Collectors.toList());
    }

    public List<ItemDetailDTO> itemsToItemDetailDTOs(List<Item> items) {
        return items.stream().filter(Objects::nonNull).map(this::itemToItemDetailDTO).collect(Collectors.toList());
    }

    public ListItemDTO itemToListItemDTO(Item item) {
        return new ListItemDTO(item);
    }

    public ItemDetailDTO itemToItemDetailDTO(Item item) {
        return new ItemDetailDTO(item);
    }
}
