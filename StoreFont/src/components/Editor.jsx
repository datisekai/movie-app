import React from 'react'
import SunEditor from 'suneditor-react';
const Editor = React.forwardRef((props, ref) => {
  return (
     <SunEditor {...props} ref={ref} />
  )
});
Editor.displayName = "Editor"

export default Editor