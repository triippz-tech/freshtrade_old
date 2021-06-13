// import React from 'react'
// import { Button, ModalBody, Modal, Row, Col, Form, Input, InputGroupAddon, InputGroup } from 'reactstrap'
// import ReactIdSwiper from 'react-id-swiper'
// import Icon from './Icon'
// import Stars from './Stars'
// import SelectBox from './SelectBox'
//
// const ModalQuickView = ({ isOpen, toggle, product }) => {
//   const [button, setButton] = React.useState(false) // State for active product option button
//   const swiperRef = React.useRef(null) // Swiper reference for slideTo method
//   const [quantity, setQuantity] = React.useState("1") // Product quantity state
//   const [currentIndex, updateCurrentIndex] = React.useState(0) // Swiper current image index
//
//   // Swiper params
//   const params = {
//     on: {
//       slideChange: () => updateCurrentIndex(swiperRef.current.swiper.realIndex)
//     }
//   }
//
//   // Slide to Swiper slide
//   const slideTo = (index) => {
//     if (swiperRef.current && swiperRef.current.swiper) {
//       swiperRef.current.swiper.slideTo(index + 1);
//       updateCurrentIndex(index)
//     }
//   }
//
//   // On quantity change
//   const onChange = (e) => {
//     const value = e.target.value;
//     setQuantity(value)
//   }
//
//   // Dummy product sizes - remove on production
//   const sizes = [
//     {
//       "value": "value_0",
//       "label": "Small"
//     },
//     {
//       "value": "value_1",
//       "label": "Medium"
//     },
//     {
//       "value": "value_2",
//       "label": "Large"
//     }
//   ]
//   return (
//     <Modal isOpen={isOpen} toggle={toggle} size="xl" modalClassName="quickview">
//
//       {/* CLOSE BUTTON */}
//       <button className="close close-absolute close-rotate" onClick={toggle} type="button">
//         <Icon className="w-3rem h-3rem svg-icon-light align-middle" icon="close-1" />
//       </button>
//       {/* END CLOSE BUTTON */}
//
//       {/* MODAL BODY */}
//       <ModalBody className="quickview-body">
//         <Row>
//           <Col lg="6">
//             <div className="detail-carousel">
//
//               {/* PRODUCT BADGES */}
//               {product.sale && <div className="product-badge badge badge-dark">Sale</div>}
//               {product.new && <div className="product-badge badge badge-secondary">Fresh</div>}
//               {product.soldout && <div className="product-badge badge badge-dark">Sold out</div>}
//               {/* END PRODUCT BADGES */}
//
//               {/* SWIPER GALLERY */}
//               <ReactIdSwiper {...params} loop ref={swiperRef}>
//                 {product.img.detail.map((image, index) =>
//                   <img className="img-fluid" src={image.img} alt={image.alt} key={index} />
//                 )}
//               </ReactIdSwiper>
//
//               {/* SWIPER THUMBS */}
//               <div className="swiper-thumbs">
//                 {product.img.detail.map((image, index) =>
//                   <button key={image.img} onClick={() => slideTo(index)} className={`swiper-thumb-item detail-thumb-item mb-3 ${currentIndex === index ? 'active' : ''}`}>
//                     <img className="img-fluid" src={image.img} alt={image.alt} />
//                   </button>
//                 )}
//               </div>
//               {/* END SWIPER THUMBS */}
//
//               {/* END SWIPER GALLERY */}
//
//             </div>
//           </Col>
//           <Col lg="6" className="p-lg-5">
//             <h2 className="mb-4 mt-4 mt-lg-1">{product.name}</h2>
//             <div className="d-flex flex-column flex-sm-row align-items-sm-center justify-content-sm-between mb-4">
//
//               {/* PRODUCT PRICES */}
//               <ul className="list-inline mb-2 mb-sm-0">
//                 <li className="list-inline-item h4 font-weight-light mb-0">${product.price.toFixed(2)}</li>
//                 <li className="list-inline-item text-muted font-weight-light">
//                   <del>$90.00</del>
//                 </li>
//               </ul>
//               {/* END PRODUCT PRICES */}
//
//               {/* PRODUCT REVIEWS STARS */}
//               <div className="d-flex align-items-center text-sm">
//                 <Stars stars={product.stars} className="mr-2 mb-0" secondColor="gray-300" />
//                 <span className="text-muted text-uppercase">25 reviews</span>
//               </div>
//               {/* END PRODUCT REVIEWS STARS */}
//
//             </div>
//
//             {/* PRODUCT DESRIPTION */}
//             <p className="mb-4 text-muted">Samsa was a travelling salesman - and above it there hung a picture that he had recently cut out of an illustrated magazine and housed in a nice, gilded frame.</p>
//             {/* END PRODUCT DESCRIPTION */}
//
//             <Form>
//               <Row>
//
//                 {/* PRODUCT SIZES */}
//                 <Col sm="6" lg="12" className="detail-option mb-4">
//                   <h6 className="detail-option-heading">Size <span>(required)</span></h6>
//                   <SelectBox options={sizes} />
//                 </Col>
//                 {/* END PRODUCT SIZES */}
//
//                 {/* PRODUCT TYPES */}
//                 <Col sm="6" lg="12" className="detail-option mb-5">
//                   <h6 className="detail-option-heading">Type <span>(required)</span></h6>
//
//                   <Button tag="label" color="outline-primary" className={`detail-option-btn-label mr-1 ${button === "value_0" ? 'active' : ''}`} size="sm" htmlFor="material_0_modal" onClick={() => setButton("value_0")}>
//                     Hoodie
//                     <Input className="input-invisible" type="radio" name="material" value="value_0" id="material_0_modal" required />
//                   </Button>
//                   <Button tag="label" color="outline-primary" className={`detail-option-btn-label ${button === "value_1" ? 'active' : ''}`} size="sm" htmlFor="material_1_modal" onClick={() => setButton("value_1")}>
//                     College
//                     <Input className="input-invisible" type="radio" name="material" value="value_1" id="material_1_modal" required />
//                   </Button>
//                 </Col>
//                 {/* END PRODUCT TYPES */}
//
//               </Row>
//
//               {/* ADD TO CART BUTTON */}
//               <InputGroup className="w-100 mb-4">
//
//                 {/* QUANTITY INPUT */}
//                 <Input bsSize="lg" className="detail-quantity" name="items" type="number" value={quantity > 0 && quantity || ''} onChange={(e) => onChange(e)} />
//                 {/* END QUANTITY INPUT */}
//
//                 {/* ADD TO CART */}
//                 <InputGroupAddon addonType="append" className="flex-grow-1">
//                   <Button color="dark" block onClick={(e) => addToCart(e, product)}><i className="fa fa-shopping-cart mr-2" />Add to Cart</Button>
//                 </InputGroupAddon>
//                 {/* END ADD TO CART */}
//
//               </InputGroup>
//               {/* END ADD TO CART BUTTON */}
//
//               <Row className="mb-4">
//                 <Col xs="6">
//
//                   {/* ADD TO WISHLIST */}
//                   <a href="#" onClick={(e) => addToWishlist(e, product)}><i className="far fa-heart mr-2" />Add to wishlist</a>
//                   {/* END ADD TO WISHLIST */}
//
//                 </Col>
//                 <Col xs="6" className="text-right">
//
//                   {/* SOCIAL */}
//                   <ul className="list-inline mb-0">
//                     <li className="list-inline-item mr-2">
//                       <a className="text-dark text-hover-primary" href="#">
//                         <i className="fab fa-facebook-f" />
//                       </a>
//                     </li>
//                     <li className="list-inline-item">
//                       <a className="text-dark text-hover-primary" href="#">
//                         <i className="fab fa-twitter" />
//                       </a>
//                     </li>
//                   </ul>
//                   {/* END SOCIAL */}
//
//                 </Col>
//               </Row>
//               <ul className="list-unstyled">
//
//                 {/* PRODUCT CATEGORIES */}
//                 <li><strong>Category:</strong> <Link href={product.category ? `/${product.category[1]}` : "/category-full"}><a onClick={toggle} className="text-muted">{product.category ? product.category[0] : 'Jeans'}</a></Link></li>
//                 {/* END PRODUCT CATEGORIES */}
//
//                 {/* PRODUCT TAGS */}
//                 <li><strong>Tags:</strong> <a className="text-muted" href="#">Leisure</a>, <a className="text-muted" href="#">Elegant</a></li>
//                 {/* END PRODUCT TAGS */}
//
//               </ul>
//             </Form>
//           </Col>
//         </Row>
//       </ModalBody>
//       {/* END MODAL BODY */}
//
//     </Modal>
//   )
// };
//
// export default ModalQuickView;
