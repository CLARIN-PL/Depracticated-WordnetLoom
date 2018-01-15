ALTER TABLE yiddish_particles
  ADD COLUMN `constituent` VARCHAR(255) NULL DEFAULT NULL AFTER `root`;