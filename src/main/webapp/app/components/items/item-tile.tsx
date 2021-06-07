import React, { useRef } from 'react';
import { IItem } from 'app/shared/model/item.model';
import { MDBCard, MDBCardBody, MDBLazyLoading, MDBLink } from 'mdbreact';
import { truncateString } from 'app/shared/util/string-utils';
import { Link } from 'react-router-dom';

export interface ItemTileProps {
  item: IItem;
}

export const ItemTile: React.FC<ItemTileProps> = props => {
  const getImageUrl = () => {
    for (let i = 0; i < props.item.images.length; i++) {
      console.log(props.item.images[i]);
      if (props.item.images[i].isVisible) return props.item.images[i].imageUrl;
    }
    return 'content/images/logo-gray.png';
  };

  return (
    <div>
      <Link to={`/items/detail/${props.item.id}`} style={{ textDecoration: 'none' }}>
        <MDBCard>
          <div className="bg-image hover-zoom ripple" data-mdb-ripple-color="light">
            <img src={getImageUrl()} className="w-100" width={50} height={120} />
          </div>
          <MDBCardBody>
            <a href="" className="text-reset">
              <h5 className="card-title mb-3">{truncateString(props.item.name, 17, false)}</h5>
            </a>
            <h6>
              {'$'}
              {props.item.price}
            </h6>
            {props.item.tradeEvent && (
              <div>
                <i className="fas fa-map-marker-alt"></i> {props.item.tradeEvent.eventName}
              </div>
            )}
            {props.item.location && (
              <div>
                <i className="fas fa-map-marker-alt"></i> {props.item.location.shortName}
              </div>
            )}
          </MDBCardBody>
        </MDBCard>
      </Link>
    </div>
  );
};

export default ItemTile;
