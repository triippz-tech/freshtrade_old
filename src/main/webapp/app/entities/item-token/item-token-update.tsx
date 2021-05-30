import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IItem } from 'app/shared/model/item.model';
import { getEntities as getItems } from 'app/entities/item/item.reducer';
import { getEntity, updateEntity, createEntity, reset } from './item-token.reducer';
import { IItemToken } from 'app/shared/model/item-token.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IItemTokenUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ItemTokenUpdate = (props: IItemTokenUpdateProps) => {
  const [idsowner, setIdsowner] = useState([]);
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { itemTokenEntity, users, items, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/item-token');
  };

  useEffect(() => {
    if (!isNew) {
      props.getEntity(props.match.params.id);
    }

    props.getUsers();
    props.getItems();
  }, []);

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
        ...itemTokenEntity,
        ...values,
        owners: mapIdList(values.owners),
        item: items.find(it => it.id.toString() === values.itemId.toString()),
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
          <h2 id="freshtradeApp.itemToken.home.createOrEditLabel" data-cy="ItemTokenCreateUpdateHeading">
            <Translate contentKey="freshtradeApp.itemToken.home.createOrEditLabel">Create or edit a ItemToken</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : itemTokenEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="item-token-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="item-token-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="tokenNameLabel" for="item-token-tokenName">
                  <Translate contentKey="freshtradeApp.itemToken.tokenName">Token Name</Translate>
                </Label>
                <AvField
                  id="item-token-tokenName"
                  data-cy="tokenName"
                  type="text"
                  name="tokenName"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="tokenCodeLabel" for="item-token-tokenCode">
                  <Translate contentKey="freshtradeApp.itemToken.tokenCode">Token Code</Translate>
                </Label>
                <AvField
                  id="item-token-tokenCode"
                  data-cy="tokenCode"
                  type="text"
                  name="tokenCode"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="createdDateLabel" for="item-token-createdDate">
                  <Translate contentKey="freshtradeApp.itemToken.createdDate">Created Date</Translate>
                </Label>
                <AvInput
                  id="item-token-createdDate"
                  data-cy="createdDate"
                  type="datetime-local"
                  className="form-control"
                  name="createdDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.itemTokenEntity.createdDate)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedDateLabel" for="item-token-updatedDate">
                  <Translate contentKey="freshtradeApp.itemToken.updatedDate">Updated Date</Translate>
                </Label>
                <AvInput
                  id="item-token-updatedDate"
                  data-cy="updatedDate"
                  type="datetime-local"
                  className="form-control"
                  name="updatedDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.itemTokenEntity.updatedDate)}
                />
              </AvGroup>
              <AvGroup>
                <Label for="item-token-owner">
                  <Translate contentKey="freshtradeApp.itemToken.owner">Owner</Translate>
                </Label>
                <AvInput
                  id="item-token-owner"
                  data-cy="owner"
                  type="select"
                  multiple
                  className="form-control"
                  name="owners"
                  value={!isNew && itemTokenEntity.owners && itemTokenEntity.owners.map(e => e.id)}
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
              <AvGroup>
                <Label for="item-token-item">
                  <Translate contentKey="freshtradeApp.itemToken.item">Item</Translate>
                </Label>
                <AvInput id="item-token-item" data-cy="item" type="select" className="form-control" name="itemId">
                  <option value="" key="0" />
                  {items
                    ? items.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/item-token" replace color="info">
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
  items: storeState.item.entities,
  itemTokenEntity: storeState.itemToken.entity,
  loading: storeState.itemToken.loading,
  updating: storeState.itemToken.updating,
  updateSuccess: storeState.itemToken.updateSuccess,
});

const mapDispatchToProps = {
  getUsers,
  getItems,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ItemTokenUpdate);
