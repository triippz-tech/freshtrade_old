import axios from 'axios';
import {
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction,
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICountry, defaultValue } from 'app/shared/model/country.model';
import { ICrudSearchAction } from 'app/config/redux-action.type';

export const ACTION_TYPES = {
  SEARCH_COUNTRY_LIST: 'country/SEARCH_COUNTRY_LIST',
  FETCH_COUNTRY_LIST_NO_PAGE: 'country/FETCH_COUNTRY_LIST_NO_PAGE',
  FETCH_COUNTRY_LIST: 'country/FETCH_COUNTRY_LIST',
  FETCH_COUNTRY: 'country/FETCH_COUNTRY',
  CREATE_COUNTRY: 'country/CREATE_COUNTRY',
  UPDATE_COUNTRY: 'country/UPDATE_COUNTRY',
  PARTIAL_UPDATE_COUNTRY: 'country/PARTIAL_UPDATE_COUNTRY',
  DELETE_COUNTRY: 'country/DELETE_COUNTRY',
  RESET: 'country/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICountry>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type CountryState = Readonly<typeof initialState>;

// Reducer

export default (state: CountryState = initialState, action): CountryState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_COUNTRY_LIST):
    case REQUEST(ACTION_TYPES.SEARCH_COUNTRY_LIST):
    case REQUEST(ACTION_TYPES.FETCH_COUNTRY_LIST_NO_PAGE):
    case REQUEST(ACTION_TYPES.FETCH_COUNTRY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_COUNTRY):
    case REQUEST(ACTION_TYPES.UPDATE_COUNTRY):
    case REQUEST(ACTION_TYPES.DELETE_COUNTRY):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_COUNTRY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_COUNTRY_LIST):
    case FAILURE(ACTION_TYPES.FETCH_COUNTRY_LIST_NO_PAGE):
    case FAILURE(ACTION_TYPES.FETCH_COUNTRY_LIST):
    case FAILURE(ACTION_TYPES.FETCH_COUNTRY):
    case FAILURE(ACTION_TYPES.CREATE_COUNTRY):
    case FAILURE(ACTION_TYPES.UPDATE_COUNTRY):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_COUNTRY):
    case FAILURE(ACTION_TYPES.DELETE_COUNTRY):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_COUNTRY_LIST): {
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_COUNTRY_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_COUNTRY_LIST_NO_PAGE): {
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_COUNTRY):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_COUNTRY):
    case SUCCESS(ACTION_TYPES.UPDATE_COUNTRY):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_COUNTRY):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_COUNTRY):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/countries';

// Actions

export const searchForEntities: ICrudSearchAction<ICountry> = (search, page, size, sort) => {
  const requestUrl = `${apiUrl}/?${search}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.SEARCH_COUNTRY_LIST,
    payload: axios.get<ICountry>(`${requestUrl}${sort && '&'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntities: ICrudGetAllAction<ICountry> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_COUNTRY_LIST,
    payload: axios.get<ICountry>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntitiesNoPage: ICrudGetAllAction<ICountry> = () => {
  return {
    type: ACTION_TYPES.FETCH_COUNTRY_LIST_NO_PAGE,
    payload: axios.get<ICountry>(`${apiUrl}/all`),
  };
};

export const getEntity: ICrudGetAction<ICountry> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_COUNTRY,
    payload: axios.get<ICountry>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ICountry> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_COUNTRY,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const updateEntity: ICrudPutAction<ICountry> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_COUNTRY,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<ICountry> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_COUNTRY,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICountry> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_COUNTRY,
    payload: axios.delete(requestUrl),
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
