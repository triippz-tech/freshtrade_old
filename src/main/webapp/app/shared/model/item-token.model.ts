import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IItem } from 'app/shared/model/item.model';
import { IReservation } from 'app/shared/model/reservation.model';

export interface IItemToken {
  id?: number;
  tokenName?: string;
  tokenCode?: string;
  createdDate?: string | null;
  updatedDate?: string | null;
  owner?: IUser | null;
  item?: IItem | null;
  reservation?: IReservation | null;
}

export const defaultValue: Readonly<IItemToken> = {};
