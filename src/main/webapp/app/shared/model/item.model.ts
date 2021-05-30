import dayjs from 'dayjs';
import { IImage } from 'app/shared/model/image.model';
import { IItemToken } from 'app/shared/model/item-token.model';
import { IUser } from 'app/shared/model/user.model';
import { ILocation } from 'app/shared/model/location.model';
import { ITradeEvent } from 'app/shared/model/trade-event.model';
import { ICategory } from 'app/shared/model/category.model';
import { Condition } from 'app/shared/model/enumerations/condition.model';

export interface IItem {
  id?: string;
  price?: number;
  quantity?: number;
  name?: string;
  details?: string;
  itemCondition?: Condition | null;
  isActive?: boolean;
  createdDate?: string | null;
  updatedDate?: string | null;
  images?: IImage[] | null;
  tokens?: IItemToken[] | null;
  owner?: IUser | null;
  location?: ILocation | null;
  tradeEvent?: ITradeEvent | null;
  categories?: ICategory[] | null;
  users?: IUser[] | null;
}

export const defaultValue: Readonly<IItem> = {
  isActive: false,
};
