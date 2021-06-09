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

import { IItem, defaultValue } from 'app/shared/model/item.model';
import { ICrudGetAllActionCriteria, ICrudReserveAction } from 'app/config/redux-action.type';
import { ItemCriteria } from 'app/shared/criteria/item-criteria';

export const ACTION_TYPES = {
  FETCH_ITEM_LIST: 'item/FETCH_ITEM_LIST',
  FETCH_ITEM: 'item/FETCH_ITEM',
  CREATE_ITEM: 'item/CREATE_ITEM',
  UPDATE_ITEM: 'item/UPDATE_ITEM',
  RESERVE_ITEM: 'item/RESERVE_ITEM',
  PARTIAL_UPDATE_ITEM: 'item/PARTIAL_UPDATE_ITEM',
  DELETE_ITEM: 'item/DELETE_ITEM',
  SET_BLOB: 'item/SET_BLOB',
  RESET: 'item/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IItem>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type ItemState = Readonly<typeof initialState>;

// Reducer

export default (state: ItemState = initialState, action): ItemState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ITEM_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ITEM):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_ITEM):
    case REQUEST(ACTION_TYPES.UPDATE_ITEM):
    case REQUEST(ACTION_TYPES.RESERVE_ITEM):
    case REQUEST(ACTION_TYPES.DELETE_ITEM):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_ITEM):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_ITEM_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ITEM):
    case FAILURE(ACTION_TYPES.CREATE_ITEM):
    case FAILURE(ACTION_TYPES.UPDATE_ITEM):
    case FAILURE(ACTION_TYPES.RESERVE_ITEM):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_ITEM):
    case FAILURE(ACTION_TYPES.DELETE_ITEM):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_ITEM_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_ITEM):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_ITEM):
    case SUCCESS(ACTION_TYPES.UPDATE_ITEM):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_ITEM):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.RESERVE_ITEM):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_ITEM):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.SET_BLOB: {
      const { name, data, contentType } = action.payload;
      return {
        ...state,
        entity: {
          ...state.entity,
          [name]: data,
          [name + 'ContentType']: contentType,
        },
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

const apiUrl = 'api/items';

// Actions
export const getEntities: ICrudGetAllAction<IItem> = (page, size, sort) => {
  const requestUrl = `${apiUrl}?${sort ? `page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ITEM_LIST,
    payload: axios.get<IItem>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntitiesWithCriteria: ICrudGetAllActionCriteria<IItem> = (criteria: ItemCriteria, page, size, sort) => {
  const requestUrl = `${apiUrl}?${criteria.generateUrl()}&${sort ? `page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ITEM_LIST,
    payload: axios.get<IItem>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getListPageEntities: ICrudGetAllActionCriteria<IItem> = (criteria: ItemCriteria, page, size, sort) => {
  let requestUrl = `${apiUrl}/list-page?${sort ? `page=${page}&size=${size}&sort=${sort}` : ''}`;
  if (criteria !== null) {
    requestUrl = `${apiUrl}/list-page?${criteria.generateUrl()}&${sort ? `page=${page}&size=${size}&sort=${sort}` : ''}`;
  }

  return {
    type: ACTION_TYPES.FETCH_ITEM_LIST,
    payload: axios.get<IItem>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntityDetail: ICrudGetAction<IItem> = id => {
  const requestUrl = `${apiUrl}/${id}/detail`;
  return {
    type: ACTION_TYPES.FETCH_ITEM,
    payload: axios.get<IItem>(requestUrl),
  };
};

export const getEntity: ICrudGetAction<IItem> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ITEM,
    payload: axios.get<IItem>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IItem> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ITEM,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const updateEntity: ICrudPutAction<IItem> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ITEM,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const reserveItems: ICrudReserveAction<IItem> = (entity, quantity) => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ITEM,
    payload: axios.put(`${apiUrl}/${entity.id}/reserve/${quantity}`),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IItem> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_ITEM,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IItem> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ITEM,
    payload: axios.delete(requestUrl),
  });
  return result;
};

export const setBlob = (name, data, contentType?) => ({
  type: ACTION_TYPES.SET_BLOB,
  payload: {
    name,
    data,
    contentType,
  },
});

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
