import axios from 'axios';
import {
  ICrudDeleteAction,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  loadMoreDataWhenScrolled,
  parseHeaderForLinks,
} from 'react-jhipster';

import { FAILURE, REQUEST, SUCCESS } from 'app/shared/reducers/action-type.util';
import { TopSellingItemsDTO } from 'app/shared/model/metrics/top-selling-items.dto';
import { defaultSellerKPIs, SellerKpis } from 'app/shared/model/metrics/seller-kpis';

export const ACTION_TYPES = {
  FETCH_SELLER_TOP_SELLING_ITEMS: 'sellerMetrics/FETCH_SELLER_TOP_SELLING_ITEMS',
  FETCH_SELLER_KPIS: 'sellerMetrics/FETCH_SELLER_KPIS',
  RESET: 'sellerMetrics/RESET',
};

const initialState = {
  loadingTotalSold: false,
  loadingKPIs: false,
  errorMessage: null,
  topSellingItems: [] as ReadonlyArray<TopSellingItemsDTO>,
  sellerKPIs: defaultSellerKPIs,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type SellerMetricsState = Readonly<typeof initialState>;

// Reducer

export default (state: SellerMetricsState = initialState, action): SellerMetricsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SELLER_TOP_SELLING_ITEMS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loadingTotalSold: true,
      };
    case REQUEST(ACTION_TYPES.FETCH_SELLER_KPIS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loadingKPIs: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_SELLER_TOP_SELLING_ITEMS):
      return {
        ...state,
        loadingTotalSold: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case FAILURE(ACTION_TYPES.FETCH_SELLER_KPIS):
      return {
        ...state,
        loadingKPIs: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_SELLER_TOP_SELLING_ITEMS): {
      return {
        ...state,
        loadingTotalSold: false,
        topSellingItems: action.payload.data,
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_SELLER_KPIS): {
      return {
        ...state,
        loadingKPIs: false,
        sellerKPIs: action.payload.data,
      };
    }
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/metrics/seller';

// Actions
export const getTopSellingItems: ICrudGetAllAction<TopSellingItemsDTO> = () => {
  return {
    type: ACTION_TYPES.FETCH_SELLER_TOP_SELLING_ITEMS,
    payload: axios.get<TopSellingItemsDTO>(`${apiUrl}/top-selling`),
  };
};

export const getSellerKPIs: ICrudGetAllAction<SellerKpis> = () => {
  return {
    type: ACTION_TYPES.FETCH_SELLER_KPIS,
    payload: axios.get<SellerKpis>(`${apiUrl}/kpis`),
  };
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
