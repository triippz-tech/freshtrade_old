import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import React from 'react';
import { Card, CardBody } from 'reactstrap';
import { IconName } from '@fortawesome/fontawesome-common-types';

interface KpiCardProps {
  cardTitle: string;
  cardData: string | number;
  icon: IconName;
  iconColor: string;
}

const KpiCard = (props: KpiCardProps) => (
  <Card>
    <CardBody>
      <div className="d-flex justify-content-between px-md-1">
        <div>
          <h3 className={props.iconColor}>{props.cardData}</h3>
          <p className="mb-0">{props.cardTitle}</p>
        </div>
        <div className="align-self-center">
          <FontAwesomeIcon icon={props.icon} color={'red'} size="3x" />
        </div>
      </div>
    </CardBody>
  </Card>
);

export default KpiCard;
