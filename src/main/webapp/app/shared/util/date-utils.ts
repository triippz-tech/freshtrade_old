import dayjs from 'dayjs';

import { APP_DATE_FORMAT, APP_LOCAL_DATETIME_FORMAT, APP_LOCAL_DATETIME_FORMAT_Z } from 'app/config/constants';

export const convertDateTimeFromServer = date => (date ? dayjs(date).format(APP_LOCAL_DATETIME_FORMAT) : null);

export const convertDateTimeToServer = date => (date ? dayjs(date).toDate() : null);

export const displayDefaultDateTime = () => dayjs().startOf('day').format(APP_LOCAL_DATETIME_FORMAT);

export const convertDateTimeFromServerToLocal = date => (date ? dayjs(date).format(APP_DATE_FORMAT) : null);

export const dateFormat = (date: Date, datePattern: string) => {
  return dayjs(date).format(datePattern);
};
