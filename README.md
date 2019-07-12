# JB-Machine

## What is it

This is an emulator of a computer, more specifically in the form in which memory is stored and updated. In this software we have the following specifications.

- LFU (Least Frequently Used) for cache policy.
- The generator of the instructions file.
- Methods of sum, subtraction and multiply.

### Results table

| Cache 1 | Cache 2 | Ram  | Hd    | Taxa cache 1 | Taxa cache 2 | Taxa ram | Taxa Hd |
| ------- | ------- | ---- | ----- | ------------ | ------------ | -------- | ------- |
| 16      | 32      | 64   | 512   | 21.58%       | 2.65%        | 34.26%   | 41.5%   |
| 16      | 32      | 64   | 64    | 13.01%       | 0.53%        | 57.58%   | 28.86   |
| 16      | 32      | 64   | 128   | 18.88%       | 0.9%         | 58.06%   | 22.15%  |
| 32      | 32      | 32   | 32    | 34.3%        | 0.16%        | 3.28%    | 62.25%  |
| 16      | 32      | 2048 | 51200 | 0.25%        | 0.15%        | 0.25%    | 99.34%  |

- 1000 instructions.

> [Website of the discipline](http://www.decom.ufop.br/decom/ensino/disciplina/bcc266/2019-1)
