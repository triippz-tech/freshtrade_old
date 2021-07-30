import dayjs from 'dayjs';
import { IItem } from 'app/shared/model/item.model';
import { IImage } from 'app/shared/model/image.model';

export interface ICategory {
  id?: string;
  slug?: string;
  name?: string;
  createdDate?: string | null;
  isActive?: boolean | null;
  isFeatured?: boolean | null;
  items?: IItem[] | null;
  images?: IImage[] | null;
}

export const defaultValue: Readonly<ICategory> = {
  isActive: false,
  isFeatured: false,
};
