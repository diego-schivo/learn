  <input
    form="<?php echo $form; ?>"
    type="<?php echo $control['type'] ?? 'text'; ?>"
    name="<?php echo $prop->getName(); ?>"
    value="<?php echo $prop->getValue($item); ?>"
    >
