import React from 'react';
import { Button, Input, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';

interface MessageUserDialogProps {
  userName: string;
  value: string;
  onChange: (msg: string) => void;
  isOpen: boolean;
  toggle: () => void;
  onSubmit: () => void;
}
export const MessageUserDialog = (props: MessageUserDialogProps) => (
  <Modal toggle={props.toggle} isOpen={props.isOpen}>
    <ModalHeader toggle={props.toggle}>
      Message <strong>{props.userName}</strong>
    </ModalHeader>
    <ModalBody>
      <Input value={props.value} onChange={event => props.onChange(event.target.value)} type="textarea" />
    </ModalBody>
    <ModalFooter>
      <Button color="secondary" onClick={() => props.toggle()}>
        Cancel
      </Button>
      <Button color="primary" onClick={() => props.onSubmit()}>
        Send Message
      </Button>
    </ModalFooter>
  </Modal>
);

export default MessageUserDialog;
