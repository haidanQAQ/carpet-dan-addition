# Release notes policy

Every GitHub release must describe **all changes since the previous release tag**, not just the
latest commit. If three features landed between `v1.5.10` and `v1.5.11`, the `v1.5.11` notes list
all three, plus version bumps, build/config changes and documentation fixes.

## Why

Release notes are the user-facing changelog. A user who jumps from `v1.5.9` to `v1.5.11` must be
able to see every behaviour change without reading the commit log. Listing only the newest commit
(or repeating features that were already released) makes the notes incomplete or misleading.

## Steps

1. Find the previous release tag:

   ```bash
   git describe --tags --abbrev=0 v1.5.11^   # -> v1.5.10
   ```

2. Generate the raw diff between that tag and the new one:

   ```bash
   scripts/changelog.sh v1.5.10 v1.5.11
   ```

3. Group the commits by user-visible feature and write the notes:

   - **New rules** - full behaviour, default value, accepted values, side effects.
   - **Changed rules** - old value/behaviour -> new value/behaviour.
   - **Fixes** - the bug, the cause, the fix.
   - **Internal** - new mixins, new classes, ticket/registry changes, dependency or build changes.
   - **Docs** - README/translation rewrites that matter to users.

4. Add the compare link:

   `https://github.com/haidanQAQ/carpet-dan-addition/compare/<prev>...<new>`

5. Publish, then diff the notes against the commit list one more time. Nothing in
   `git log <prev>..<new>` may be missing unless it is purely cosmetic.

## Example

`v1.5.11` notes must cover everything in `git log v1.5.10..v1.5.11` - the `TNTchunkloading` rule,
the new `TicketType`, the README/lang updates and the version bump - even though the release is
"about" TNT chunk loading.
