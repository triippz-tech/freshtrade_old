import React from 'react';

import ReactIdSwiper from 'react-id-swiper';

import Lightbox from 'react-image-lightbox';
import 'react-image-lightbox/style.css';
import Magnifier from 'react-magnifier';
import { Button } from 'reactstrap';
import 'swiper/swiper.scss';
import { IImage } from 'app/shared/model/image.model';
import { IItem } from 'app/shared/model/item.model';

interface SwiperGalleryProps {
  images: IImage[];
}

const SwiperGallery = (props: SwiperGalleryProps) => {
  const [lightBoxOpen, setLightBoxOpen] = React.useState(false);
  const [activeImage, setActiveImage] = React.useState(0);
  const [activeSlide, setActiveSlide] = React.useState(0);

  const ref = React.useRef(null);

  const visibleImages = (): IImage[] => {
    const images = props.images.map(image => {
      if (image.isVisible) return image;
    });

    if (images.length === 0)
      return [
        {
          id: 'NONE',
          imageUrl: 'content/images/logo-gray.png',
          isVisible: true,
        },
      ];
    return images;
  };

  const onClick = index => {
    setActiveImage(index);
    setLightBoxOpen(!lightBoxOpen);
  };

  const slideTo = index => {
    setActiveSlide(index);
    if (ref.current !== null && ref.current.swiper !== null) {
      ref.current.swiper.slideToLoop(index);
    }
  };

  const params = {
    slidesPerView: 1,
    spaceBetween: 0,
    loop: true,
    on: {
      slideChange: () => setActiveSlide(ref.current.swiper.realIndex),
    },
  };
  const customStyles = {
    overlay: {
      zIndex: '1000',
    },
    bodyOpen: {
      position: 'fixed',
    },
  };
  return (
    <React.Fragment>
      <div className="detail-carousel">
        <ReactIdSwiper {...params} ref={ref}>
          {props.images &&
            visibleImages().map((item, index) => {
              if (item.isVisible)
                return (
                  <div key={index}>
                    <Magnifier
                      mgShowOverflow={false}
                      mgWidth={2000}
                      mgHeight={2000}
                      className="img-fluid"
                      src={item.imageUrl}
                      zoomFactor={0.11}
                    />
                    <Button color="photoswipe" onClick={() => onClick(index)}>
                      <i className="fa fa-expand" aria-hidden="true"></i>
                    </Button>
                  </div>
                );
            })}
        </ReactIdSwiper>
      </div>
      {lightBoxOpen && (
        <Lightbox
          mainSrc={visibleImages()[activeImage].imageUrl}
          nextSrc={visibleImages()[(activeImage + 1) % visibleImages().length].imageUrl}
          prevSrc={visibleImages()[(activeImage + visibleImages().length - 1) % visibleImages().length].imageUrl}
          onCloseRequest={() => setLightBoxOpen(false)}
          onMovePrevRequest={() => setActiveImage((activeImage + visibleImages().length - 1) % visibleImages().length)}
          onMoveNextRequest={() => setActiveImage((activeImage + 1) % visibleImages().length)}
          enableZoom={false}
          reactModalStyle={customStyles}
        />
      )}
      <div className="swiper-thumbs">
        {props.images &&
          visibleImages().map((image, index) => {
            if (image.isVisible)
              return (
                <button
                  key={index}
                  onClick={() => slideTo(index)}
                  className={`swiper-thumb-item detail-thumb-item mb-3 ${activeSlide === index ? 'active' : ''}`}
                >
                  <img className="img-fluid" src={image.imageUrl} alt={image.id} />
                </button>
              );
          })}
      </div>
    </React.Fragment>
  );
};

export default SwiperGallery;
