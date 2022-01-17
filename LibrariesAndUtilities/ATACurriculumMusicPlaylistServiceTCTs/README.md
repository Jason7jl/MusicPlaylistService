### Developing TCTs

#### Update `definition.yaml` in ATACurriculumMusicPlaylistServiceLambda
Replace
```yaml
ATACurriculumMusicPlaylistServiceTCTs:
  type: hydra
  versionSet: ATACurriculumMusicPlaylistServiceLambda/development
  runDefinitionURI: mounts/hydra/runDefinition.json
  packages:
    ATACurriculumMusicPlaylistServiceTCTs:
      majorVersion: V2Cohort1
      isApplicationTarget: true
```

with
```yaml
ATACurriculumMusicPlaylistServiceTCTs:
  type: hydra
  versionSet: ATACurriculumMusicPlaylistServiceLambda/development
  runDefinitionURI: mounts/hydra/runDefinition.json
  codeURI: ../../
  packages:
    ATACurriculumMusicPlaylistServiceTCTs:
      buildTarget: release
      isApplicationTarget: true
```

#### Update stack
Run `rde stack provision`

#### Profit
