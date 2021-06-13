import React from 'react';

import { Badge } from 'reactstrap';

import { IItem } from 'app/shared/model/item.model';
import { Link } from 'react-router-dom';

interface CardProductProps {
  product: IItem;
  masonry?: any;
}
const CardProduct = ({ product, masonry }: CardProductProps) => {
  const [quickView, setQuickView] = React.useState(false);

  return (
    <div className="product" data-aos="zoom-in" data-aos-delay="0">
      <div className="product-image mb-md-3">
        <Link to={`/items/detail/${product.id}`}>
          <a>
            {/* {product.images.length < 1 ? */}
            {/*   masonry ? */}
            {/*     <img className="img-fluid" src={product.images.} alt={product.img.masonry.alt} /> */}
            {/*     : */}
            {/*     <div className="product-swap-image"> */}
            {/*       <img className="img-fluid product-swap-image-front" src={product.img.category[0].img} alt={product.img.category[0].alt} /> */}
            {/*       <img className="img-fluid" src={product.img.category[1].img} alt={product.img.category[1].alt} /> */}
            {/*     </div> */}
            {/*   : */}
            {/*   <img className="img-fluid" src={product.img.category[0].img} alt="product" /> */}
            {/* } */}
            <img
              className="img-fluid"
              src={product.images.length !== 0 ? product.images[0].imageUrl : 'content/images/logo-gray.png'}
              alt="product"
            />
          </a>
        </Link>
      </div>
      <div className="position-relative">
        <h3 className="text-base mb-1">
          <Link to={`/items/detail/${product.id}`}>
            <a className="text-dark">{product.name}</a>
          </Link>
        </h3>
        <span className="text-gray-500 text-sm">${product.price}.00</span>
        <span className="text-gray-500 text-sm">
          {product.tradeEvent && (
            <div>
              <i className="fas fa-map-marker-alt"></i> {product.tradeEvent.eventName}
            </div>
          )}
        </span>
      </div>
    </div>
  );
};

export default CardProduct;
