import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntities as getItems } from 'app/entities/item/item.reducer';
import { getEntity, updateEntity, createEntity, reset } from './category.reducer';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { IImage } from 'app/shared/model/image.model';

export interface ICategoryUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CategoryUpdate = (props: ICategoryUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);
  const [inputs, setInputs] = useState<Array<string>>(['input-0']);

  const { categoryEntity, items, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/category');
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

    const images: Array<IImage> = inputs
      .map((val, idx) => {
        const value = values[val] === null ? null : values[val];
        delete values[val];
        console.log(value);
        const img: IImage = {
          imageUrl: value,
        };
        return img;
      })
      .filter(function (el) {
        return el != null;
      });

    if (errors.length === 0) {
      const entity = {
        ...categoryEntity,
        ...values,
        images: images /* eslint object-shorthand: 0 */,
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  const addAnotherImage = () => {
    const lastIn: string = inputs[inputs.length - 1];
    const num: number = Number.parseInt(lastIn[lastIn.length - 1], 2);
    const newNum = num + 1;

    setInputs([...inputs, `input-${newNum}`]);
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="freshtradeApp.category.home.createOrEditLabel" data-cy="CategoryCreateUpdateHeading">
            <Translate contentKey="freshtradeApp.category.home.createOrEditLabel">Create or edit a Category</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : categoryEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="category-id">
                    <Translate contentKey="freshtradeApp.category.id">Id</Translate>
                  </Label>
                  <AvInput id="category-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="slugLabel" for="category-slug">
                  <Translate contentKey="freshtradeApp.category.slug">Slug</Translate>
                </Label>
                <AvField
                  id="category-slug"
                  data-cy="slug"
                  type="text"
                  name="slug"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 50, errorMessage: translate('entity.validation.maxlength', { max: 50 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="nameLabel" for="category-name">
                  <Translate contentKey="freshtradeApp.category.name">Name</Translate>
                </Label>
                <AvField
                  id="category-name"
                  data-cy="name"
                  type="text"
                  name="name"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 50, errorMessage: translate('entity.validation.maxlength', { max: 50 }) },
                  }}
                />
              </AvGroup>
              <AvGroup hidden>
                <Label id="createdDateLabel" for="category-createdDate">
                  <Translate contentKey="freshtradeApp.category.createdDate">Created Date</Translate>
                </Label>
                <AvInput
                  id="category-createdDate"
                  data-cy="createdDate"
                  type="datetime-local"
                  className="form-control"
                  name="createdDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.categoryEntity.createdDate)}
                />
              </AvGroup>
              <AvGroup>
                {inputs.map((val, idx) => (
                  <div key={val}>
                    <Label id="detailsLabel" for={val}>
                      Image {idx + 1}
                    </Label>
                    <AvInput
                      id={val}
                      data-cy={val}
                      type="string"
                      name={val}
                      validate={{
                        pattern: {
                          value: '[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)',
                          errorMessage: 'Must provide a valid URL for image',
                        },
                      }}
                    />
                  </div>
                ))}
              </AvGroup>
              <AvGroup>
                <Button onClick={addAnotherImage} size="sm">
                  Add More Images
                </Button>
              </AvGroup>
              <AvGroup check>
                <Label id="isActiveLabel">
                  <AvInput id="category-isActive" data-cy="isActive" type="checkbox" className="form-check-input" name="isActive" />
                  <Translate contentKey="freshtradeApp.category.isActive">Is Active</Translate>
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="isFeaturedLabel">
                  <AvInput id="category-isFeatured" data-cy="isFeatured" type="checkbox" className="form-check-input" name="isFeatured" />
                  <Translate contentKey="freshtradeApp.category.isFeatured">Is Featured</Translate>
                </Label>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/category" replace color="info">
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
  categoryEntity: storeState.category.entity,
  loading: storeState.category.loading,
  updating: storeState.category.updating,
  updateSuccess: storeState.category.updateSuccess,
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

export default connect(mapStateToProps, mapDispatchToProps)(CategoryUpdate);
