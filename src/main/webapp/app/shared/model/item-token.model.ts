import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IItem } from 'app/shared/model/item.model';

export interface IItemToken {
  id?: number;
  tokenName?: string;
  tokenCode?: string;
  createdDate?: string | null;
  updatedDate?: string | null;
  owners?: IUser[] | null;
  item?: IItem | null;
}

export const defaultValue: Readonly<IItemToken> = {};
