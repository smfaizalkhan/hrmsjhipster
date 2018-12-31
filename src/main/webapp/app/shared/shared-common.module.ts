import { NgModule } from '@angular/core';

import { HrmsSharedLibsModule, FindLanguageFromKeyPipe, JhiAlertComponent, JhiAlertErrorComponent } from './';
import { SearchComponent } from './search/search-component';

@NgModule({
    imports: [HrmsSharedLibsModule],
    declarations: [FindLanguageFromKeyPipe, SearchComponent, JhiAlertComponent, JhiAlertErrorComponent],
    exports: [HrmsSharedLibsModule, FindLanguageFromKeyPipe, JhiAlertComponent, JhiAlertErrorComponent, SearchComponent]
})
export class HrmsSharedCommonModule {}
