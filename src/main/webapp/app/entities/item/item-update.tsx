import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { setFileData, byteSize, Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { ILocation } from 'app/shared/model/location.model';
import { getEntities as getLocations } from 'app/entities/location/location.reducer';
import { ITradeEvent } from 'app/shared/model/trade-event.model';
import { getEntities as getTradeEvents } from 'app/entities/trade-event/trade-event.reducer';
import { ICategory } from 'app/shared/model/category.model';
import { getEntities as getCategories } from 'app/entities/category/category.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './item.reducer';
import { IItem } from 'app/shared/model/item.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IItemUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ItemUpdate = (props: IItemUpdateProps) => {
  const [idsuser, setIdsuser] = useState([]);
  const [idscategories, setIdscategories] = useState([]);
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { itemEntity, users, locations, tradeEvents, categories, loading, updating } = props;

  const { details } = itemEntity;

  const handleClose = () => {
    props.history.push('/item');
  };

  useEffect(() => {
    if (!isNew) {
      props.getEntity(props.match.params.id);
    }

    props.getUsers();
    props.getLocations();
    props.getTradeEvents();
    props.getCategories();
  }, []);

  const onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => props.setBlob(name, data, contentType), isAnImage);
  };

  const clearBlob = name => () => {
    props.setBlob(name, undefined, undefined);
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.updatedDate = convertDateTimeToServer(values.updatedDate);

    if (errors.length === 0) {
      const entity = {
        ...itemEntity,
        ...values,
        users: mapIdList(values.users),
        categories: mapIdList(values.categories),
        owner: users.find(it => it.id.toString() === values.ownerId.toString()),
        location: locations.find(it => it.id.toString() === values.locationId.toString()),
        tradeEvent: tradeEvents.find(it => it.id.toString() === values.tradeEventId.toString()),
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="freshtradeApp.item.home.createOrEditLabel" data-cy="ItemCreateUpdateHeading">
            <Translate contentKey="freshtradeApp.item.home.createOrEditLabel">Create or edit a Item</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : itemEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="item-id">
                    <Translate contentKey="freshtradeApp.item.id">Id</Translate>
                  </Label>
                  <AvInput id="item-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="priceLabel" for="item-price">
                  <Translate contentKey="freshtradeApp.item.price">Price</Translate>
                </Label>
                <AvField
                  id="item-price"
                  data-cy="price"
                  type="string"
                  className="form-control"
                  name="price"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="quantityLabel" for="item-quantity">
                  <Translate contentKey="freshtradeApp.item.quantity">Quantity</Translate>
                </Label>
                <AvField
                  id="item-quantity"
                  data-cy="quantity"
                  type="string"
                  className="form-control"
                  name="quantity"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    min: { value: 1, errorMessage: translate('entity.validation.min', { min: 1 }) },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="nameLabel" for="item-name">
                  <Translate contentKey="freshtradeApp.item.name">Name</Translate>
                </Label>
                <AvField
                  id="item-name"
                  data-cy="name"
                  type="text"
                  name="name"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="detailsLabel" for="item-details">
                  <Translate contentKey="freshtradeApp.item.details">Details</Translate>
                </Label>
                <AvInput
                  id="item-details"
                  data-cy="details"
                  type="textarea"
                  name="details"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="itemConditionLabel" for="item-itemCondition">
                  <Translate contentKey="freshtradeApp.item.itemCondition">Item Condition</Translate>
                </Label>
                <AvInput
                  id="item-itemCondition"
                  data-cy="itemCondition"
                  type="select"
                  className="form-control"
                  name="itemCondition"
                  value={(!isNew && itemEntity.itemCondition) || 'NEW'}
                >
                  <option value="NEW">{translate('freshtradeApp.Condition.NEW')}</option>
                  <option value="OPEN_BOX">{translate('freshtradeApp.Condition.OPEN_BOX')}</option>
                  <option value="LIKE_NEW">{translate('freshtradeApp.Condition.LIKE_NEW')}</option>
                  <option value="USED">{translate('freshtradeApp.Condition.USED')}</option>
                  <option value="DAMAGED">{translate('freshtradeApp.Condition.DAMAGED')}</option>
                  <option value="FOR_PARTS">{translate('freshtradeApp.Condition.FOR_PARTS')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup check>
                <Label id="isActiveLabel">
                  <AvInput id="item-isActive" data-cy="isActive" type="checkbox" className="form-check-input" name="isActive" />
                  <Translate contentKey="freshtradeApp.item.isActive">Is Active</Translate>
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="createdDateLabel" for="item-createdDate">
                  <Translate contentKey="freshtradeApp.item.createdDate">Created Date</Translate>
                </Label>
                <AvInput
                  id="item-createdDate"
                  data-cy="createdDate"
                  type="datetime-local"
                  className="form-control"
                  name="createdDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.itemEntity.createdDate)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedDateLabel" for="item-updatedDate">
                  <Translate contentKey="freshtradeApp.item.updatedDate">Updated Date</Translate>
                </Label>
                <AvInput
                  id="item-updatedDate"
                  data-cy="updatedDate"
                  type="datetime-local"
                  className="form-control"
                  name="updatedDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.itemEntity.updatedDate)}
                />
              </AvGroup>
              <AvGroup>
                <Label for="item-owner">
                  <Translate contentKey="freshtradeApp.item.owner">Owner</Translate>
                </Label>
                <AvInput id="item-owner" data-cy="owner" type="select" className="form-control" name="ownerId">
                  <option value="" key="0" />
                  {users
                    ? users.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="item-location">
                  <Translate contentKey="freshtradeApp.item.location">Location</Translate>
                </Label>
                <AvInput id="item-location" data-cy="location" type="select" className="form-control" name="locationId">
                  <option value="" key="0" />
                  {locations
                    ? locations.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="item-tradeEvent">
                  <Translate contentKey="freshtradeApp.item.tradeEvent">Trade Event</Translate>
                </Label>
                <AvInput id="item-tradeEvent" data-cy="tradeEvent" type="select" className="form-control" name="tradeEventId">
                  <option value="" key="0" />
                  {tradeEvents
                    ? tradeEvents.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="item-categories">
                  <Translate contentKey="freshtradeApp.item.categories">Categories</Translate>
                </Label>
                <AvInput
                  id="item-categories"
                  data-cy="categories"
                  type="select"
                  multiple
                  className="form-control"
                  name="categories"
                  value={!isNew && itemEntity.categories && itemEntity.categories.map(e => e.id)}
                >
                  <option value="" key="0" />
                  {categories
                    ? categories.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="item-user">
                  <Translate contentKey="freshtradeApp.item.user">User</Translate>
                </Label>
                <AvInput
                  id="item-user"
                  data-cy="user"
                  type="select"
                  multiple
                  className="form-control"
                  name="users"
                  value={!isNew && itemEntity.users && itemEntity.users.map(e => e.id)}
                >
                  <option value="" key="0" />
                  {users
                    ? users.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/item" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  users: storeState.userManagement.users,
  locations: storeState.location.entities,
  tradeEvents: storeState.tradeEvent.entities,
  categories: storeState.category.entities,
  itemEntity: storeState.item.entity,
  loading: storeState.item.loading,
  updating: storeState.item.updating,
  updateSuccess: storeState.item.updateSuccess,
});

const mapDispatchToProps = {
  getUsers,
  getLocations,
  getTradeEvents,
  getCategories,
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ItemUpdate);
