<?php
  $control = $controls[$prop->getName()];
?>

<?php include ($control['element'] ?? 'input') . '.php'; ?>
