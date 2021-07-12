import { IItem } from 'app/shared/model/item.model';

export interface TopSellingItemsDTO {
  item?: IItem | null;
  totalSold?: number;
}
