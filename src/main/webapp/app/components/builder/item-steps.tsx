import React, { useMemo } from 'react';
import { Label } from 'reactstrap';
import { translate, Translate } from 'react-jhipster';
import { AvFeedback, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';

export const ItemStep1 = () => (
  <AvGroup>
    <Label id="nameLabel" for="item-name">
      <Translate contentKey="freshtradeApp.item.create.name">Name</Translate>
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
);
