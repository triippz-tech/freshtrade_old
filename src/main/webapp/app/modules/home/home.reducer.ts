import axios from 'axios';

import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { IItem } from 'app/shared/model/item.model';
import { ICategory } from 'app/shared/model/category.model';
import { ITradeEvent } from 'app/shared/model/trade-event.model';
import { ICrudGetAllAction } from 'react-jhipster';
import { IImage } from 'app/shared/model/image.model';

export const ACTION_TYPES = {
  FETCH_FEATURED_ITEMS: 'homePage/FETCH_FEATURED_ITEMS',
  FETCH_FEATURED_CATEGORIES: 'homePage/FETCH_FEATURED_CATEGORIES',
  FETCH_TRENDING_ITEMS: 'homePage/FETCH_TRENDING_ITEMS',
  FETCH_NEARBY_EVENTS: 'homePage/FETCH_NEARBY_EVENTS',
};

const initialState = {
  loading: false,
  errorMessage: null,
  featuredItems: [] as ReadonlyArray<IItem>,
  trendingItems: [] as ReadonlyArray<IItem>,
  featuredCategories: [] as ReadonlyArray<ICategory>,
  nearbyEvents: [] as ReadonlyArray<ITradeEvent>,
};

export type HomePageState = Readonly<typeof initialState>;

// Reducer

export default (state: HomePageState = initialState, action): HomePageState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_FEATURED_CATEGORIES):
    case REQUEST(ACTION_TYPES.FETCH_FEATURED_ITEMS):
    case REQUEST(ACTION_TYPES.FETCH_NEARBY_EVENTS):
    case REQUEST(ACTION_TYPES.FETCH_TRENDING_ITEMS):
      return {
        ...state,
        errorMessage: null,
        loading: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_FEATURED_CATEGORIES):
    case FAILURE(ACTION_TYPES.FETCH_NEARBY_EVENTS):
    case FAILURE(ACTION_TYPES.FETCH_FEATURED_ITEMS):
    case FAILURE(ACTION_TYPES.FETCH_TRENDING_ITEMS):
      return {
        ...state,
        loading: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_FEATURED_CATEGORIES):
      return {
        ...state,
        loading: false,
        featuredCategories: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_NEARBY_EVENTS):
      return {
        ...state,
        loading: false,
        nearbyEvents: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_FEATURED_ITEMS):
      return {
        ...state,
        loading: false,
        featuredItems: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_TRENDING_ITEMS):
      return {
        ...state,
        loading: false,
        trendingItems: action.payload.data,
      };
    default:
      return state;
  }
};

const itemAPI = 'api/items';
const categoryAPI = 'api/category';
const tradeEventApi = 'api/trade-event';

// Actions

export const getTrendingItems: ICrudGetAllAction<IItem> = (page, size, sort) => {
  const requestUrl = `${itemAPI}/trending${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_TRENDING_ITEMS,
    payload: axios.get<IItem>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getFeaturedItems: ICrudGetAllAction<IItem> = (page, size, sort) => {
  const requestUrl = `${itemAPI}/featured${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_FEATURED_ITEMS,
    payload: axios.get<IItem>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};
