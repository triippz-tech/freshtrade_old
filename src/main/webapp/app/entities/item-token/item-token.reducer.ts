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

import { IItemToken, defaultValue } from 'app/shared/model/item-token.model';

export const ACTION_TYPES = {
  FETCH_ITEMTOKEN_LIST: 'itemToken/FETCH_ITEMTOKEN_LIST',
  FETCH_ITEMTOKEN: 'itemToken/FETCH_ITEMTOKEN',
  CREATE_ITEMTOKEN: 'itemToken/CREATE_ITEMTOKEN',
  UPDATE_ITEMTOKEN: 'itemToken/UPDATE_ITEMTOKEN',
  PARTIAL_UPDATE_ITEMTOKEN: 'itemToken/PARTIAL_UPDATE_ITEMTOKEN',
  DELETE_ITEMTOKEN: 'itemToken/DELETE_ITEMTOKEN',
  RESET: 'itemToken/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IItemToken>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type ItemTokenState = Readonly<typeof initialState>;

// Reducer

export default (state: ItemTokenState = initialState, action): ItemTokenState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ITEMTOKEN_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ITEMTOKEN):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_ITEMTOKEN):
    case REQUEST(ACTION_TYPES.UPDATE_ITEMTOKEN):
    case REQUEST(ACTION_TYPES.DELETE_ITEMTOKEN):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_ITEMTOKEN):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_ITEMTOKEN_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ITEMTOKEN):
    case FAILURE(ACTION_TYPES.CREATE_ITEMTOKEN):
    case FAILURE(ACTION_TYPES.UPDATE_ITEMTOKEN):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_ITEMTOKEN):
    case FAILURE(ACTION_TYPES.DELETE_ITEMTOKEN):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_ITEMTOKEN_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_ITEMTOKEN):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_ITEMTOKEN):
    case SUCCESS(ACTION_TYPES.UPDATE_ITEMTOKEN):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_ITEMTOKEN):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_ITEMTOKEN):
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

const apiUrl = 'api/item-tokens';

// Actions

export const getEntities: ICrudGetAllAction<IItemToken> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ITEMTOKEN_LIST,
    payload: axios.get<IItemToken>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IItemToken> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ITEMTOKEN,
    payload: axios.get<IItemToken>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IItemToken> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ITEMTOKEN,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const updateEntity: ICrudPutAction<IItemToken> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ITEMTOKEN,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IItemToken> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_ITEMTOKEN,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IItemToken> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ITEMTOKEN,
    payload: axios.delete(requestUrl),
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
