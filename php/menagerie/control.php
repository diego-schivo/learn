<?php
  $control = $meta['properties'][$prop->getName()]['control'];
?>

<?php include ($control['element'] ?? 'input') . '.php'; ?>
