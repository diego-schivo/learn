  <select
    form="<?php echo $form; ?>"
    name="<?php echo $prop->getName(); ?>"
    >
    <option value=""></option>
    <?php foreach ($control['options'] as $option) { ?>
      <option
        value="<?php echo $option; ?>"
        <?php echo ($prop->getValue($item) === $option) ? 'selected' : ''; ?>
        >
        <?php echo $option; ?>
      </option>
    <?php } ?>
  </select>
