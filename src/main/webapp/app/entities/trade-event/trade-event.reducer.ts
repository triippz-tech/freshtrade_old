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

import { ITradeEvent, defaultValue } from 'app/shared/model/trade-event.model';
import { ICrudLoadOptionsAction } from 'app/config/redux-action.type';

export const ACTION_TYPES = {
  SEARCH_TRADEEVENTS: 'item/SEARCH_TRADEEVENTS',
  FETCH_TRADEEVENT_LIST: 'tradeEvent/FETCH_TRADEEVENT_LIST',
  FETCH_TRADEEVENT: 'tradeEvent/FETCH_TRADEEVENT',
  CREATE_TRADEEVENT: 'tradeEvent/CREATE_TRADEEVENT',
  UPDATE_TRADEEVENT: 'tradeEvent/UPDATE_TRADEEVENT',
  PARTIAL_UPDATE_TRADEEVENT: 'tradeEvent/PARTIAL_UPDATE_TRADEEVENT',
  DELETE_TRADEEVENT: 'tradeEvent/DELETE_TRADEEVENT',
  RESET: 'tradeEvent/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ITradeEvent>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type TradeEventState = Readonly<typeof initialState>;

// Reducer

export default (state: TradeEventState = initialState, action): TradeEventState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_TRADEEVENT_LIST):
    case REQUEST(ACTION_TYPES.SEARCH_TRADEEVENTS):
    case REQUEST(ACTION_TYPES.FETCH_TRADEEVENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_TRADEEVENT):
    case REQUEST(ACTION_TYPES.UPDATE_TRADEEVENT):
    case REQUEST(ACTION_TYPES.DELETE_TRADEEVENT):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_TRADEEVENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_TRADEEVENTS):
    case FAILURE(ACTION_TYPES.FETCH_TRADEEVENT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_TRADEEVENT):
    case FAILURE(ACTION_TYPES.CREATE_TRADEEVENT):
    case FAILURE(ACTION_TYPES.UPDATE_TRADEEVENT):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_TRADEEVENT):
    case FAILURE(ACTION_TYPES.DELETE_TRADEEVENT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_TRADEEVENTS): {
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_TRADEEVENT_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_TRADEEVENT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_TRADEEVENT):
    case SUCCESS(ACTION_TYPES.UPDATE_TRADEEVENT):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_TRADEEVENT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_TRADEEVENT):
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

const apiUrl = 'api/trade-events';

// Actions

export const getEntities: ICrudGetAllAction<ITradeEvent> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_TRADEEVENT_LIST,
    payload: axios.get<ITradeEvent>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const loadEvents: ICrudLoadOptionsAction<ITradeEvent> = (inputValue, callBack) => async dispatch => {
  console.log(inputValue);
  const requestUrl = `api/_search/trade-events${inputValue ? `?query=${inputValue}` : ''}`;
  const result = await dispatch({
    type: ACTION_TYPES.SEARCH_TRADEEVENTS,
    payload: axios.get<ITradeEvent>(`${requestUrl}`),
  });
  callBack(
    result.value.data.map(val => {
      return {
        label: val.eventName,
        value: val.eventName,
        event: val,
      };
    })
  );
  return result;
};

export const getEntity: ICrudGetAction<ITradeEvent> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_TRADEEVENT,
    payload: axios.get<ITradeEvent>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ITradeEvent> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_TRADEEVENT,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const updateEntity: ICrudPutAction<ITradeEvent> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_TRADEEVENT,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<ITradeEvent> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_TRADEEVENT,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ITradeEvent> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_TRADEEVENT,
    payload: axios.delete(requestUrl),
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
