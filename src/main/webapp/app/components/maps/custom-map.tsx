import React from 'react';
import L from 'leaflet';
import { MapContainer, Marker, Popup, TileLayer } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import './map.scss';

import icon from 'leaflet/dist/images/marker-icon.png';
import iconShadow from 'leaflet/dist/images/marker-shadow.png';

export interface MapProps {
  lat?: number;
  lng?: number;
  popupText?: string;
}

const defaultProps: MapProps = {
  lat: 40.0690416,
  lng: -76.4637013,
  popupText: 'FreshTrade!',
};

const DefaultIcon = L.icon({
  iconUrl: icon,
  shadowUrl: iconShadow,
});

L.Marker.prototype.options.icon = DefaultIcon;

export const CustomMap: React.FC<MapProps> = props => {
  return (
    <MapContainer center={[props.lat, props.lng]} zoom={13} scrollWheelZoom={true} style={{ width: '100%', height: '100vh' }}>
      <TileLayer
        attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />
      <Marker position={[props.lat, props.lng]}>
        <Popup>{props.popupText}</Popup>
      </Marker>
    </MapContainer>
  );
};

CustomMap.defaultProps = defaultProps;

export default React.memo(CustomMap);
