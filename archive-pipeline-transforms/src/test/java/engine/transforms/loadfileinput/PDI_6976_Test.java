/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2017 by Hitachi Vantara : http://www.pentaho.com
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/

package org.apache.hop.pipeline.transforms.loadfileinput;

import junit.framework.TestCase;
import org.apache.hop.core.CheckResultInterface;
import org.apache.hop.core.fileinput.FileInputList;
import org.apache.hop.core.row.RowMetaInterface;
import org.apache.hop.core.variables.VariableSpace;
import org.apache.hop.metastore.api.IMetaStore;
import org.apache.hop.pipeline.PipelineMeta;
import org.apache.hop.pipeline.transform.TransformMeta;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.tools.FileObject;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * Tests for LoadFileInputMeta class
 *
 * @author Pavel Sakun
 * @see LoadFileInputMeta
 */
public class PDI_6976_Test {
  @Test
  public void testVerifyNoPreviousTransform() {
    LoadFileInputMeta spy = spy( new LoadFileInputMeta() );

    FileInputList fileInputList = mock( FileInputList.class );
    List<FileObject> files = when( mock( List.class ).size() ).thenReturn( 1 ).getMock();
    doReturn( files ).when( fileInputList ).getFiles();
    doReturn( fileInputList ).when( spy ).getFiles( any( VariableSpace.class ) );

    @SuppressWarnings( "unchecked" )
    List<CheckResultInterface> validationResults = mock( List.class );

    // Check we do not get validation errors
    doAnswer( new Answer<Object>() {
      @Override
      public Object answer( InvocationOnMock invocation ) throws Throwable {
        if ( ( (CheckResultInterface) invocation.getArguments()[ 0 ] ).getType() != CheckResultInterface.TYPE_RESULT_OK ) {
          TestCase.fail( "We've got validation error" );
        }

        return null;
      }
    } ).when( validationResults ).add( any( CheckResultInterface.class ) );

    spy.check( validationResults, mock( PipelineMeta.class ), mock( TransformMeta.class ), mock( RowMetaInterface.class ),
      new String[] {}, new String[] { "File content", "File size" }, mock( RowMetaInterface.class ),
      mock( VariableSpace.class ), mock( IMetaStore.class ) );
  }
}