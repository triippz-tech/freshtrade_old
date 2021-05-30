import dayjs from 'dayjs';
import { IItem } from 'app/shared/model/item.model';

export interface ICategory {
  id?: string;
  slug?: string;
  name?: string;
  createdDate?: string | null;
  isActive?: boolean | null;
  items?: IItem[] | null;
}

export const defaultValue: Readonly<ICategory> = {
  isActive: false,
};
