import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IItem } from 'app/shared/model/item.model';
import { getEntities as getItems } from 'app/entities/item/item.reducer';
import { getEntity, updateEntity, createEntity, reset } from './image.reducer';
import { IImage } from 'app/shared/model/image.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IImageUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ImageUpdate = (props: IImageUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { imageEntity, items, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/image');
  };

  useEffect(() => {
    if (!isNew) {
      props.getEntity(props.match.params.id);
    }

    props.getItems();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.createdDate = convertDateTimeToServer(values.createdDate);

    if (errors.length === 0) {
      const entity = {
        ...imageEntity,
        ...values,
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
          <h2 id="freshtradeApp.image.home.createOrEditLabel" data-cy="ImageCreateUpdateHeading">
            <Translate contentKey="freshtradeApp.image.home.createOrEditLabel">Create or edit a Image</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : imageEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="image-id">
                    <Translate contentKey="freshtradeApp.image.id">Id</Translate>
                  </Label>
                  <AvInput id="image-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="imageUrlLabel" for="image-imageUrl">
                  <Translate contentKey="freshtradeApp.image.imageUrl">Image Url</Translate>
                </Label>
                <AvField id="image-imageUrl" data-cy="imageUrl" type="text" name="imageUrl" />
              </AvGroup>
              <AvGroup>
                <Label id="createdDateLabel" for="image-createdDate">
                  <Translate contentKey="freshtradeApp.image.createdDate">Created Date</Translate>
                </Label>
                <AvInput
                  id="image-createdDate"
                  data-cy="createdDate"
                  type="datetime-local"
                  className="form-control"
                  name="createdDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.imageEntity.createdDate)}
                />
              </AvGroup>
              <AvGroup check>
                <Label id="isVisibleLabel">
                  <AvInput id="image-isVisible" data-cy="isVisible" type="checkbox" className="form-check-input" name="isVisible" />
                  <Translate contentKey="freshtradeApp.image.isVisible">Is Visible</Translate>
                </Label>
              </AvGroup>
              <AvGroup>
                <Label for="image-item">
                  <Translate contentKey="freshtradeApp.image.item">Item</Translate>
                </Label>
                <AvInput id="image-item" data-cy="item" type="select" className="form-control" name="itemId">
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
              <Button tag={Link} id="cancel-save" to="/image" replace color="info">
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
  items: storeState.item.entities,
  imageEntity: storeState.image.entity,
  loading: storeState.image.loading,
  updating: storeState.image.updating,
  updateSuccess: storeState.image.updateSuccess,
});

const mapDispatchToProps = {
  getItems,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ImageUpdate);
